import {Component, OnInit} from '@angular/core';
import {UploaderService} from "../uploader/uploader.service";
import {ActivatedRoute, Router, ParamMap} from "@angular/router";
import 'rxjs/add/operator/switchMap';
import {Utils} from "../utils/utils";
import {MatExpansionModule} from '@angular/material/expansion';
import {BigService} from "../big.service";


@Component({
    selector: 'app-status-list',
    templateUrl: './status-list.component.html',
    styleUrls: ['./status-list.component.css']
})
export class StatusListComponent implements OnInit {

    subscription: any;
    threads : any;
    statuses : any;
    true: boolean = true;
    data : any;
    loading = true;


    constructor(private _fileUploadedService: UploaderService,
                private route: ActivatedRoute,
                private router: Router,
                private bigService : BigService) {
    }


    ngOnInit() {
        let timestamp = this.route.snapshot.paramMap.get('time');
        this.bigService.getThreadListTimestamp(timestamp).subscribe(data => {
            this.data = Utils.parseData(data);
            this.init();
        });
    }

    init() {
        let records = this.data;
        this.statuses = Utils.getStatuses(this.data);
        this.threads = {};
        for(let status of this.statuses) {
            this.threads[status] = records.filter(entry => entry.status == status);
        }
    }

    onChartInit(name) {
        // ec.showLoading();
        // console.log("I'm being called")
        // this.bigService.getThreadDataByName(name).subscribe(data => {
            // ec.hideLoading();
            console.log("got " + name);
            this.loading = false;
            var xAxis = this.bigService.parsedDataValue.filter(entry => entry.name == name).map(entry => {return [entry.time, entry.status]});
            xAxis.sort((a, b) => a[0] - b[0]);
            var values = xAxis.map(entry => entry[1]);
            xAxis = xAxis.map(entry => {return Utils.formatTimestep(entry[0])});

            const series = {
                name: name,
                type:'line',
                data: values
            };
            return {
                title: {
                    text: 'Thread Status Over Time'
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap : false,
                    axisLine: {onZero: false},
                    data: xAxis
                },
                yAxis: {
                    type: 'category',
                    data: ["WAITING", "TIMED_WAITING", "RUNNABLE", "BLOCKED"]
                },
                series: series
            };
    }

}
