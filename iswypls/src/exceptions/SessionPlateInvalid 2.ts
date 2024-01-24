export class SessionPlateInvalid extends Error {

    constructor(plate: string) {
        super(`the plate ${plate} is invalid'`);
    }
}