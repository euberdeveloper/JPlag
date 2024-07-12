import * as fs from 'fs';

class Try {
    public static main(args: string[]): void {
        new Try().load("DoesNotExist.txt");
    }

    public load(path: string): void {
        let scanner: fs.ReadStream | null = null;
        try {
            let other: fs.ReadStream; // This is just here to keep the tokens similar.
            scanner = fs.createReadStream(path);
            scanner.on('data', (chunk: Buffer) => {
                console.log(chunk.toString());
            });
            scanner.on('error', (err: Error) => {
                console.error(err);
            });
        } catch (exception) {
            console.error(exception);
        } finally {
            if (scanner !== null) {
                scanner.close();
            }
        }
    }
}

Try.main(process.argv.slice(2));

