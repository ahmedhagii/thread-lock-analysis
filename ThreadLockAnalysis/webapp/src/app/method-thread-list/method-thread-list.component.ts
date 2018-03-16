import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UploaderService} from "../uploader/uploader.service";
import {BigService} from "../big.service";
import {Utils} from "../utils/utils";

@Component({
  selector: 'app-method-thread-list',
  templateUrl: './method-thread-list.component.html',
  styleUrls: ['./method-thread-list.component.css']
})
export class MethodThreadListComponent implements OnInit {

    data: any;
    threads : any;
    true: boolean = true;
    period : Number;

    constructor(private _fileUploadedService: UploaderService,
                private route: ActivatedRoute,
                private router: Router,
                private bigService : BigService) {
    }


    ngOnInit() {
        let methodName = this.route.snapshot.paramMap.get('methodName');
        this.bigService.getMethodData(methodName).subscribe(data => {
            this.init(data);
        });
    }

    init(data) {
        // let data = this.bigService.parsedDataValue;
        let records = Utils.parseData(data);
        this.period = records[records.length - 1].time - records[0].time;
        let map = new Map();

        records.map(entry => {
            let obj = JSON.stringify([entry.name, entry.status]);
            if(!map.has(obj)) {
                map.set(obj,[]);
            }
            var old = map.get(obj);
            old.push(entry.time);
            map.set(obj, old);
        });
        // console.log(map);
        let seen =  new Set();
        records = records.filter(entry => {
            let obj = JSON.stringify([entry.name, entry.status]);
            return !seen.has(obj) && map.get(obj).sort() && seen.add(obj);
        });

        records = records.map(entry => {
            let obj = JSON.stringify([entry.name, entry.status]);
            let times = map.get(obj);
            // console.log(obj, times);
            entry.timeSpent = (parseInt(times[times.length - 1]) - parseInt(times[0]));
            // console.log(entry);
            return entry;
        });
        // console.log(records);
        this.threads = records.sort((a, b) => a.name < b.name);
    }
}
