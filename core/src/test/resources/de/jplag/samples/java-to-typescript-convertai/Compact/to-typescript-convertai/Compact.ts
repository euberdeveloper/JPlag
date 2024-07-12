import { ArrayList } from 'java.util';

export class Compact {
    static tryCount(a: ArrayList<number>, n: number): number {
        if (a.isEmpty()) {
            return Number.MAX_SAFE_INTEGER;
        }

        let count = 0;

        for (let i = 0; i + 1 < a.size(); i++) {
            if (a.get(i) + 1 !== a.get(i + 1)) {
                count++;
            }
        }

        if (a.get(0) !== 0) {
            count++;
        }

        if (a.get(a.size() - 1) !== n - 1) {
            count++;
        }

        return count;
    }
}

