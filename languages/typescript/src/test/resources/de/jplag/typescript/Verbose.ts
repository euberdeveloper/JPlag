import { ArrayList } from 'typescript-collections';

export class Verbose {
    static tryCount(l: ArrayList<number>, n: number): number {
        if (l.size() === 0) {
            return Number.MAX_SAFE_INTEGER;
        }

        let count = 0;

        for (let i = 0; i + 1 < l.size(); i++) {
            if (l.get(i) + 1 !== l.get(i + 1)) {
                count++;
            }
        }
        if (l.get(0) !== 0) {
            count++;
        }

        if (l.get(l.size() - 1) !== n - 1) {
            count++;
        }

        return count;
    }
}

