import {IsNotEmpty, IsPositive, IsString} from 'class-validator';
import { v4 as uuidv4 } from 'uuid';

export class SessionDto {

    @IsNotEmpty()
    @IsPositive()
    id: number;

    @IsNotEmpty()
    @IsString()
    plate: string;

    @IsNotEmpty()
    @IsString()
    date: string;

    constructor(plate: string, date: string) {
        this.id = this.generateId();
        this.plate = plate;
        this.date = date;
    }

    private generateId(): number {
        return uuidv4();
    }

    getId(): number {
        return this.id;
    }

    getPlate(): string {
        return this.plate;
    }

    getDate(): string {
        return this.date;
    }

    private computeDiff(): number {
        const date = new Date(this.date);
        const now = new Date();
        return now.getTime() - date.getTime();
    }


}