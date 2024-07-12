class PatternMatchingManual {
    private static readonly Test: (x: number) => { x: number } = (x) => ({ x });

    test(): void {
        const a: unknown = new this.constructor.Test(1);
        if (a instanceof this.constructor.Test) {
            const testA: { x: number } = a;
        }
    }
}

