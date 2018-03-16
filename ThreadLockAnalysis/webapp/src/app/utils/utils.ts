export class Utils {
    static parseRow(val: string) {
        let splits = val.split(",");
        let ret = {
            id: splits[0],
            name: splits[1],
            prio: splits[2],
            status: splits[3],
            time: splits[4],
            stacktrace: (String(splits[5])).replace(/###\$\$###/g, "\n")
        };
        return ret;
    }
    static parseData(data) {
        return data.map((row : string) => this.parseRow(row));
    }
    static formatTimestep(timestep : string) {
        let date = new Date(parseInt(timestep)*1000);
        return date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear() + "\n" +
            date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }
    static getStatuses(parsedData) {
        var counter = {};
        parsedData.forEach((v, i) => {
            if(!counter[v.status]) {
                counter[v.status] = 1
            }else {
                counter[v.status] += 1
            }
        });
        return Object.keys(counter);
    }
    static data : any;
}
