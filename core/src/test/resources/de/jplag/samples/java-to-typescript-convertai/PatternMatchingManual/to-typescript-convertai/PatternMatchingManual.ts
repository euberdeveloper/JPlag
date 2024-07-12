class PatternMatchingManual {
    private static readonly Test: (x: number) => { x: number } = (x) => ({ x });

    test(): void {
        const a: unknown = { x: 1 };
        if (a instanceof PatternMatchingManual.Test) {
            const testA: { x: number } = a;
        }
    }
}

