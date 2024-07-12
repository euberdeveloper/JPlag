class StringConcat {
    test(): void {
        let param1: number = 1;
        let param2: string = "test";

        let result: string = `prefix ${param1} infix ${param2.length()} suffix`;
    }
}

