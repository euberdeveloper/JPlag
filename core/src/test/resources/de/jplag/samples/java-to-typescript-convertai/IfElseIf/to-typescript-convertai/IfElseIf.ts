import { isNullOrUndefined } from 'util';

if (isNullOrUndefined(process.argv.slice(2))) {
    throw new Error('Illegal argument exception');
} else if (process.argv.slice(2).length > 1) {
    console.log(process.argv.slice(2).toString());
}

