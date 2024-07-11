import { IllegalArgumentException } from 'typescript-collections';

if (process.argv.length <= 2) {
    throw new IllegalArgumentException();
}

console.log(process.argv.slice(2).join(', '));

