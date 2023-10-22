import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser'

@Pipe({
    name: 'highlight'
})

export class HighlightSearch implements PipeTransform {

    constructor(private sanitized: DomSanitizer) {}

    transform(value: string, args: string): any {
        if (!args) {return value;}
        let l = args.length;
        let temp = "";
        if (args.length <= value.length) {
            temp = value;
            for (let i = 0; i <= value.length-l; i++) {
                if (i === value.length-l) {
                    if (this.convert(value).substring(i) === this.convert(args)) {
                        temp = temp.replace(temp.substring(i), '<span class="highlight">$&</span>')
                    }
                } else {
                    if (this.convert(value).substring(i, i+l) === this.convert(args)) {
                        temp = temp.replace(temp.substring(i, i+l), '<span class="highlight">$&</span>')
                        return this.sanitized.bypassSecurityTrustHtml(temp);
                    }
                }
            }
        }

        return this.sanitized.bypassSecurityTrustHtml(temp);
    }

    convert(input: string): string {

        input = input.toLowerCase();
        input = input.normalize("NFD").replace(/[\u0300-\u036f]/g, "")
        input = input.replace("ç","c");
        input = input.replace("æ","ae");
        input = input.replace("œ","oe");

        return input;
    }
}