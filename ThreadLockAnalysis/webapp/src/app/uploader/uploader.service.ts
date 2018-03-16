import {EventEmitter, Injectable} from '@angular/core';
import {Utils} from "../utils/utils";
import {Subject} from "rxjs/Subject";

@Injectable()
export class UploaderService {

    data : any = new Subject();
    actualData : any;
    ready : boolean = false;
    parsedData : Object[];

    constructor() {}

    emitDataChange(data) {
        console.log('emit');
        console.log(data)
        this.actualData = data;
        this.ready = true;
        this.parsedData = Utils.parseData(data.all);
        // this.data.emit(data);
        this.data.next(data.all);
    }
    getNavChangeEmitter() {
        return this.data.asObservable();
    }

}
