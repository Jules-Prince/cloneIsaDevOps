export class SessionAlreadyExist extends Error {

    constructor(plate: string) {
        super(`the plate ${plate} has already a session`);
    }
}