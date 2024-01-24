import {IsNotEmpty, IsPositive, IsString} from "class-validator";

export class ParcDto {
    @IsNotEmpty()
    @IsString()
    plate: string;

    @IsNotEmpty()
    @IsPositive()
    remainingTime: number;
}