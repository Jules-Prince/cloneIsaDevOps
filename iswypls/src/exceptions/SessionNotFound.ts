export class SessionNotFound extends Error {

    constructor(plate: string) {
        super(`the given plate has not been found : ${plate}`);
    }
}