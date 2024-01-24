import {Get, Injectable} from '@nestjs/common';
import {SessionDto} from './dto/session.dto';
import {SessionAlreadyExist} from "./exceptions/SessionAlreadyExist";
import {SessionPlateInvalid} from "./exceptions/SessionPlateInvalid";
import {SessionNotFound} from "./exceptions/SessionNotFound";
import {ParcDto} from "./dto/parc.dto";
import axios from "axios";

@Injectable()
export class AppService {

    private sessions: Array<SessionDto>
    PLATE_REGEX = "^[A-Z]{2}[A-Z0-9]{8}$";

    constructor() {
        this.sessions = [];
    }

    getAllSessions(): SessionDto[] {
        return this.sessions;
    }

    getSessionByPlate(plate: string): SessionDto {
        let session = this.sessions.find(session => session.getPlate() === plate);
        console.log(session);
        if (session === undefined) {
            console.log("session not found");
            throw new SessionNotFound(plate);
        }
        return session;
    }

    createSession(parcDto: ParcDto): SessionDto {
        try {
            console.log("Create session iswypls");
            console.log(parcDto);
            this.canSessionBeCreated(parcDto.plate);
        } catch (error) {
            console.log("Error creation of a session iswypls");
            throw error;
        }

        let session = new SessionDto(parcDto.plate, new Date().toISOString());
        console.log("Session create successfully with plate"+session.plate+" at "+session.date);
        this.sessions.push(session);

        parcDto.remainingTime = 0;
        setTimeout(async () => {
            this.sessions.pop();
            console.log("The session will be valid for another 5 minutes : "+session.plate +" call multi fidelity api");
            await axios.put('http://server:8081/api/iswypls', parcDto);
        }, 60000*25);

        parcDto.remainingTime = 5;
        setTimeout(async () => {
            this.sessions.pop();
            console.log("End of the session : "+session.plate +" call multi fidelity api");
            await axios.put('http://server:8081/api/iswypls', parcDto);
        }, 60000*30);

        return session;
    }


    private canSessionBeCreated(plate: string) {
        try {
            console.log("check if session exists");
            this.checkIfSessionExists(plate);
            console.log("check if plate is valid");
            this.isPlateValid(plate);
        } catch (error) {
            throw error;
        }
    }

    private checkIfSessionExists(plate: string) {
        const session = this.sessions.find(session => session.getPlate() === plate);
        if (!(session === undefined)) {
            throw new SessionAlreadyExist(plate);
        }
    }

    private isPlateValid(plate: string) {
        const regex = new RegExp(this.PLATE_REGEX);
        if (!regex.test(plate)) {
            console.log("plate is invalid");
            throw new SessionPlateInvalid(plate);
        }
    }
}
