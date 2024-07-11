import { IllegalArgumentException } from 'java/lang';
import { Arrays } from 'java/util';

function main(args: string[]): void {
    if (args === null) {
        throw new IllegalArgumentException();
    } else if (args.length > 1) {
        console.log(Arrays.toString(args));
    } else {
        console.log(args[0]);
    }
}

