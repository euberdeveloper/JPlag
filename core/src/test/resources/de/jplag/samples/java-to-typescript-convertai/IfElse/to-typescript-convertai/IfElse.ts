import { argv } from 'process';

if (argv === null) {
    throw new Error('Illegal argument exception');
} else {
    console.log(argv);
}

