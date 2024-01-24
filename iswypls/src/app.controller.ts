// @ts-ignore
import {Body, Controller, Get, HttpException, HttpStatus, Param, Post} from '@nestjs/common';
import {AppService} from './app.service';
import {SessionDto} from "./dto/session.dto";
import {ParcDto} from "./dto/parc.dto";

@Controller('iswypls')
export class AppController {
    constructor(private readonly appService: AppService) {
    }

    @Get('sessions')
    getAllSessions(): SessionDto[] {
        return this.appService.getAllSessions();
    }

    @Get('sessions/:plate')
    getSession(@Param('plate') plate): SessionDto {
        try {
            return this.appService.getSessionByPlate(plate);
        } catch (error) {
            throw new HttpException('iswypls error: ' + error.message, HttpStatus.NOT_FOUND);
        }
    }

    @Post()
    createSession(@Body() parcDto: ParcDto): SessionDto {
        try {
            return this.appService.createSession(parcDto);
        } catch (error) {
            throw new HttpException('iswypls error: ' + error.message, HttpStatus.FORBIDDEN);
        }
    }

}
