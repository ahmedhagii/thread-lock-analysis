import {Component, OnInit, ViewChild, ElementRef} from '@angular/core';
import {UploaderService} from "../uploader/uploader.service";
import {MatDialog, MatDialogRef} from "@angular/material";
import {ChartDialogComponent} from "../chart-dialog/chart-dialog.component";
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import {BigService} from "../big.service";


@Component({
    selector: 'cool-chart',
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {
    @ViewChild('chart') el: ElementRef;
    @ViewChild('chart2') chart2: ElementRef;
    @ViewChild('chart3') chart3: ElementRef;
    @ViewChild('chart4') chart4: ElementRef;
    // @ViewChild('chart5') chart5: ElementRef;

    subscription: any;
    data: any;
    dialogRef: MatDialogRef<ChartDialogComponent>;
    chartOption: any;
    statusCharts : any[] = [];
    prioCharts : any[] = [];
    showSep : boolean = true;
    threadCount: Number;
    period: any = {from: 0, to: 0};
    parsedData : any;
    methods : any;
    loading : boolean = true;
    threadsOverTimeChart : any;

    functionMapping = {
        'freqOverTime' : this.freqOverTime,
        'statusOverTime': this.statusOverTime,
        'piechart': this.piechart,
    };

    constructor(private _fileUploadedService: UploaderService, public dialog: MatDialog,
                private route : ActivatedRoute, private router: Router,
                private _bigService : BigService) {}

    ngOnInit() {
        // this.basicChart();
        // this.chartOption = {
        //     title: {
        //         text: '堆叠区域图'
        //     },
        //     tooltip : {
        //         trigger: 'axis'
        //     },
        //     legend: {
        //         data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
        //     },
        //     toolbox: {
        //         feature: {
        //             saveAsImage: {}
        //         }
        //     },
        //     grid: {
        //         left: '3%',
        //         right: '4%',
        //         bottom: '3%',
        //         containLabel: true
        //     },
        //     xAxis : [
        //         {
        //             type : 'category',
        //             boundaryGap : false,
        //             data : ['周一','周二','周三','周四','周五','周六','周日']
        //         }
        //     ],
        //     yAxis : [
        //         {
        //             type : 'value'
        //         }
        //     ],
        //     series : [
        //         {
        //             name:'邮件营销',
        //             type:'line',
        //             stack: '总量',
        //             areaStyle: {normal: {}},
        //             data:[120, 132, 101, 134, 90, 230, 210]
        //         },
        //         {
        //             name:'联盟广告',
        //             type:'line',
        //             stack: '总量',
        //             areaStyle: {normal: {}},
        //             data:[220, 182, 191, 234, 290, 330, 310]
        //         },
        //         {
        //             name:'视频广告',
        //             type:'line',
        //             stack: '总量',
        //             areaStyle: {normal: {}},
        //             data:[150, 232, 201, 154, 190, 330, 410]
        //         },
        //         {
        //             name:'直接访问',
        //             type:'line',
        //             stack: '总量',
        //             areaStyle: {normal: {}},
        //             data:[320, 332, 301, 334, 390, 330, 320]
        //         },
        //         {
        //             name:'搜索引擎',
        //             type:'line',
        //             stack: '总量',
        //             label: {
        //                 normal: {
        //                     show: true,
        //                     position: 'top'
        //                 }
        //             },
        //             areaStyle: {normal: {}},
        //             data:[820, 932, 901, 934, 1290, 1330, 1320]
        //         }
        //     ]
        // }
        if(this._bigService.parsedDataValue) {
            let data = this._bigService.parsedDataValue;
            console.log(data);
            this.loading = false;
            this.threadsOverTimeChart = this.getThreadsOverTimeChart(data);
            this.prioCharts.push(this.option(data));
            this.statusCharts.push(this.piechart(data));
            this.statusCharts.push(this.statusOverTime(data));
            this.showSep = false;
            this.threadCount = this.getThreadCount(data);
            this.period = this.getPeriod(data);

            this.methods = this.getMethods(data);
        }
        else {
            this._bigService.waitForData().subscribe(data => {
                // this.data = data;
                console.log(data);
                // this.parsedData = this._fileUploadedService.parsedData;
                this.loading = false;
                this.threadsOverTimeChart = this.getThreadsOverTimeChart(data);
                this.prioCharts.push(this.option(data));
                this.statusCharts.push(this.piechart(data));
                this.statusCharts.push(this.statusOverTime(data));
                this.showSep = false;
                this.threadCount = this.getThreadCount(data);
                this.period = this.getPeriod(data);

                this.methods = this.getMethods(data);

            });
        }

    }

    openDialog(name) {
        this.dialogRef = this.dialog.open(ChartDialogComponent, {
            data: this.functionMapping[name](this.data),
        });
    }


    freqOverTime(dataPassed) {
        let pairs = dataPassed.all.map(row => {
            let splits = row.split(",");
            return {time: splits[4], prio: splits[2]}
        });

        let uniqueTimestamps =  Array.from(new Set(dataPassed.all.map(row => {
            let splits = row.split(",");
            return splits[4]
        }))).sort();


        let filterPrios = (uniqueTimestamps, prio) => {
            return uniqueTimestamps.map(t => {
                return pairs.filter(p => p.time == t && p.prio == prio).length
            })
        };

        const trace1 = {
            x: uniqueTimestamps.map((tt : string) => new Date(parseInt(tt)*1000)),
            y: filterPrios(uniqueTimestamps, 10),
            name: "Priority: 10",
            mode: 'lines+markers',
            marker: {
                size: 6
            },
            line: {
                width: 1
            },
        };

        const trace2 = {
            x: uniqueTimestamps.map((tt : string)  => new Date(parseInt(tt)*1000)),
            y: filterPrios(uniqueTimestamps, 5),
            name: "Priority: 5",
            mode: 'lines+markers',
            marker: {
                size: 6
            },
            line: {
                width: 1
            },
        };

        const trace3 = {
            x: uniqueTimestamps.map((ts : string) => {
                var ts_int = parseInt(ts);
                return new Date(ts_int*1000)
            }),
            y: filterPrios(uniqueTimestamps, 1),
            name: "Priority: 1",
            mode: 'lines+markers',
            marker: {
                // color: 'rgb(128, 0, 128)',
                size: 6
            },
            line: {
                // color: 'rgb(128, 0, 128)',
                width: 1
            },
        };
        this.chartOption = {
            xAxis: {
                type: 'time',
                data: uniqueTimestamps.map((ts : string) => {
                    var ts_int = parseInt(ts);
                    return new Date(ts_int*1000)
                })
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: filterPrios(uniqueTimestamps, 5),
                type: 'line'
            }]
        };

        var data = [trace1, trace2, trace3];
        var layout = {
            title: 'Frequency of Diff Thread Priorities Over Time',
            width: 500,
            height: 400,
            xaxis: {
                title: 'Time',
                titlefont: {
                    family: 'Courier New, monospace',
                    size: 18,
                    color: '#7f7f7f'
                }
            },
            yaxis: {
                title: 'Count',
                titlefont: {
                    family: 'Courier New, monospace',
                    size: 18,
                    color: '#7f7f7f'
                }
            },
            margin: {r: 0}
        };
        return {data: data, layout: layout}
    }

    getThreadsOverTimeChart(data) {
        // let pairs = data.map(row => {
        //     // let splits = row.split(",");
        //     return {time: row.time, prio: row.prio}
        // });

        let uniqueTimestamps =  Array.from(new Set(data.map(row => {
            return row.time;
        }))).sort();

        // let filterPrios = (uniqueTimestamps, prio) => {
        //     return uniqueTimestamps.map(t => {
        //         return pairs.filter(p => p.time == t && p.prio == prio).length
        //     })
        // };
        //
        let yData = [];
        let timestamps = this.formatTimestamps(uniqueTimestamps);
        uniqueTimestamps.map(ts => {
            yData.push(data.filter(entry => entry.time == ts).length);
        });


        return {
            title: {
                text: 'Frequency of Threads Over Time'
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
                data: timestamps
            },
            yAxis: {
                type: 'value',
            },
            series: [
                {
                    // name:'Priority 5',
                    type:'line',
                    // stack: 'Priority 5',
                    data: yData
                },
            ]
        };
    }
    option(data) {
        // let pairs = data.map(row => {
        //     // let splits = row.split(",");
        //     return {time: row.time, prio: row.prio}
        // });
        //
        // let uniqueTimestamps =  Array.from(new Set(data.map(row => {
        //     return row.time;
        // }))).sort();
        //
        // let filterPrios = (uniqueTimestamps, prio) => {
        //     return uniqueTimestamps.map(t => {
        //         return pairs.filter(p => p.time == t && p.prio == prio).length
        //     })
        // };
        //
        // let timestamps = uniqueTimestamps.map((ts : string) => {
        //     var ts_int = parseInt(ts);
        //     let date = new Date(ts_int*1000);
        //     return date.getFullYear() + "/" + date.getMonth() + "/" + date.getDay() + "\n" + date.getHours() + ":" + date.getMinutes();
        // });

        let parsedData = data;
        let uniqueTimestamps = this.uniqueTimestamps(parsedData);
        let timestamps = this.formatTimestamps(uniqueTimestamps);

        let filterPrio = (uniqueTimestamps, prio) => {
            return uniqueTimestamps.map(t => {
                return parsedData.filter(p => p.time == t && p.prio == prio).length
            })
        };

        var counter = {};
        parsedData.forEach((v, i) => {
            if(v.prio != -1) {
                if(!counter[v.prio]) {
                    counter[v.prio] = 1
                }else {
                    counter[v.prio] += 1
                }
            }
        });
        var plotData = [];
        for(let key of Object.keys(counter)) {
            const series = {
                name: "Priority " + key,
                type:'line',
                stack: "Priority " + key,
                data: filterPrio(uniqueTimestamps, key),
            };
            plotData.push(series);
        }
        let keys = Object.keys(counter).map(key => 'Priority ' + key);
        console.log(keys);
        return {
            title: {
                text: 'Frequency of Diff Thread Priorities Over Time'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: keys,
                top: '10%'
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
                data: timestamps
            },
            yAxis: {
                type: 'value'
            },
            series: plotData
        };

    }



    parseData(data) {
        let parsedData = data.all.map(row => {
            let splits = row.split(",");
            return {id: splits[0], name: splits[1], prio: splits[3], status: splits[3],
                time: splits[4]}
        });
        return parsedData;
    }
    uniqueTimestamps(parsedData) {
        let uniqueTimestamps =  Array.from(new Set(parsedData.map(row => {
            return row.time;
        }))).sort();
        return uniqueTimestamps
    }
    formatTimestamps(uniqueTimeStamps) {
        let timestamps = uniqueTimeStamps.map((ts : string) => {
            var ts_int = parseInt(ts);
            let date = new Date(ts_int*1000);
            return  (date.getMonth()+1) + "-" + date.getDate() + "-" + date.getFullYear() + "\n" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        });
        return timestamps;
    }
    getThreadCount(data) {
        let parsedData = data;
        let threadCount =  Array.from(new Set(parsedData.map(row => {
            return row.id;
        }))).length;
        return threadCount;
    }
    getPeriod(data) {
        let parsedData = data;
        let ts = this.formatTimestamps(this.uniqueTimestamps(parsedData))
        return {from: ts[0], to: ts[ts.length - 2]};
    }
    onClick(event) {
        let clickedStatus = event.name;
        console.log(event);
        this.router.navigate([('/thread-summary/status/' + clickedStatus)]);
    }

    // status charts
    piechart(data) {
        let parsedData = data;

        var counter = {};
        parsedData.forEach((v, i) => {
            if(!counter[v.status]) {
                counter[v.status] = 1
            }else {
                counter[v.status] += 1
            }
        });

        let plotData = [];
        for(let e of Object.keys(counter)) {
            let obj = {};
            obj['name'] = e;
            obj['value'] = counter[e];
            plotData.push(obj);
        }
        return {
            title : {
                text: 'Status Distribution',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: Object.keys(counter)
            },
            series : [
                {
                    name: 'Status',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data: plotData,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
    }
    statusOverTime(data) {
        let parsedData = data;
        let uniqueTimestamps = this.uniqueTimestamps(parsedData);
        let timestamps = this.formatTimestamps(uniqueTimestamps);

        let filterStatus = (uniqueTimestamps, status) => {
            return uniqueTimestamps.map(t => {
                return parsedData.filter(p => p.time == t && p.status == status).length
            })
        };

        var counter = {};
        parsedData.forEach((v, i) => {
            if(!counter[v.status]) {
                counter[v.status] = 1
            }else {
                counter[v.status] += 1
            }
        });
        var plotData = [];
        for(let key of Object.keys(counter)) {
            const series = {
                name: key,
                type:'line',
                stack: key,
                data: filterStatus(uniqueTimestamps, key),
            };
            plotData.push(series);
        }
        return {
            title: {
                text: 'Thread Status Over Time'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: Object.keys(counter),
                top: '10%'
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
                data: timestamps
            },
            yAxis: {
                type: 'value'
            },
            series: plotData
        };
    }

    getMethods(data) {
        let methods = data
            .map(entry => entry.stacktrace.split("\n")[0].replace("at ", ""))
            .filter(method => method != "" && method != "None");

        let counter = {}
        for(let m of methods) {
            if(!counter[m]) counter[m] = 1;
            else counter[m]++;
        }

        let ret = [];
        for(let m of Object.keys(counter)) {
            ret.push({name: m, count: counter[m]});
        }
        ret.sort((a, b) => b.count - a.count);
        console.log(ret);
        return ret;
    }

    onMethodClick(event, methodName) {
        console.log(methodName);
        this.router.navigate(['/threads/method/', methodName]);
    }
}

