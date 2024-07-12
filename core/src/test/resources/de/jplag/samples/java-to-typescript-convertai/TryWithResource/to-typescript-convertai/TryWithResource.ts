import * as fs from 'fs';

class TryWithResource {
    public static main(args: string[]): void {
        new TryWithResource().load("DoesNotExist.txt");
    }

    public load(path: string): void {
        let other: fs.Scanner | null = null; // This is just here to keep the tokens similar.
        try {
            const scanner: fs.Scanner = other = new fs.Scanner(fs.createReadStream(path));
            while (scanner.hasNext()) {
                console.log(scanner.nextLine());
            }
        } catch (exception) {
            if (exception instanceof fs.FileNotFoundException) {
                exception.stack?.toString();
            }
        } finally {
            if (other !== null) { // This as well...
                other.close(); // This as well...
            }
        }
    }
}

