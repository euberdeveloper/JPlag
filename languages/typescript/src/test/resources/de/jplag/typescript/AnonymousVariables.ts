class AnonymousVariables {
    test(o: any): void {
        let _: string = "";

        if (typeof o === "string") {
            let _: string = o;
        }
    }
}

