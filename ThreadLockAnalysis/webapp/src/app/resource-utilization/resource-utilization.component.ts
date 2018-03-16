import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BigService} from "../big.service";
import {ActivatedRoute} from "@angular/router";
import {Utils} from "../utils/utils";
// import {chart} from 'highcharts';
// import * as Highcharts from 'highcharts';
// import * as HighChartsTreeMap from 'highcharts/modules/treemap';
import { Chart } from 'angular-highcharts';

@Component({
    selector: 'app-resource-utilization',
    templateUrl: './resource-utilization.component.html',
    styleUrls: ['./resource-utilization.component.css']
})
export class ResourceUtilizationComponent implements AfterViewInit {
    // @ViewChild('chartTarget') chartTarget: ElementRef;
    // @ViewChild('parallel') parallelTarget: ElementRef;
    // @ViewChild('treemap') treemapTarget: ElementRef;
    resourcesUtil: any;
    tree : any;


    chart: Chart = new Chart({});
    parallel: Chart;
    treemap : Chart = new Chart({});
    streamgraph : Chart = new Chart();
    sanky : Chart = new Chart();

    constructor(private _bigservice: BigService,
                private route: ActivatedRoute) {
    }

    ngAfterViewInit() {
        let dumpName = this.route.snapshot.paramMap.get('dumpName');
        // this.chart = ;
        // this.parallel = new Chart();
        // this.treemap  = new Chart();
        this._bigservice.getResourcesUtil(dumpName).subscribe(data => {
            // let res = this.getResourcesUtil(data);
            // this.resourcesUtil = res[0];

            this.chart = new Chart(this.getLineChart(data));
        });

        this._bigservice.getTreeMapData(dumpName).subscribe(data => {
            // this.treemap = new Chart(this.getTreeMap(data));
            this.treemap = new Chart(this.getTreeMap(data));
        });

        this._bigservice.getStreamGraph(dumpName).subscribe(data => {
            this.streamgraph = new Chart(this.getStreamGraph(data));
        });

        this._bigservice.getTreeData(dumpName).subscribe(data => {
            console.log(data);
            this.tree = this.getTree(data);
            this.sanky = new Chart(this.getSanky(data));
        });
    }

    getResourcesUtil(data) {
        data = data.counts;
        let timestamps = Object.keys(data).map(x => Utils.formatTimestep(x));
        let timestamps2 = Object.keys(data).map(x => new Date(parseInt(x) * 1000));

        let resourceData = Object.values(data);
        let chartData = {};
        chartData['seriesData'] = [];
        for (let datapoint of resourceData) {
            for (let key of Object.keys(datapoint)) {
                if (!chartData[key]) chartData[key] = [];
                chartData[key].push(datapoint[key])
            }
        }


        for (let key of Object.keys(chartData)) {
            if (key == 'seriesData') continue;
            const series = {
                name: key,
                type: 'line',
                stack: key,
                data: chartData[key],
            };
            chartData['seriesData'].push(series);
        }

        let echarts = {
            title: {
                text: 'Resources Utilization Over Time',
                // top: '10%',
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: Object.keys(chartData),
                // top: '95%',
                bottom: '3%',
                // padding: [20, 5],
                align: 'right',
                right: '0',
                // left: '',
                orient: 'vertical'
            },
            grid: {
                left: '3%',
                right: '4%',
                // bottom: '3%',
                containLabel: true,
                width: '70%',
                // height: '90%'
            },
            // toolbox: {
            //     feature: {
            //         saveAsImage: {}
            //     }
            // },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                axisLine: {onZero: false},
                data: timestamps
            },
            yAxis: {
                type: 'value'
            },
            series: chartData['seriesData']
        };
        let highchart = {
            chart: {
                renderTo: 'container',
                type: 'line'
            },
            title: {
                text: 'Resources Utilization Over Time',
            },

            subtitle: {
                text: 'Source: thesolarfoundation.com'
            },

            yAxis: {
                title: {
                    text: 'Amount of locks'
                }
            },
            xAxis: {
                type: 'datetime',
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            plotOptions: {
                series: {
                    label: {
                        connectorAllowed: false
                    },
                    // pointStart: 2010
                }
            },


            series: chartData['seriesData'].map(elem => {
                let ret = [];
                for (let index in elem.data) {
                    ret.push([timestamps2[index], elem.data[index]]);
                }
                elem.data = ret;
                return elem;
            }),

            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }
        };


        return [echarts, highchart];
    }

    getLineChart(data) {
        data = data.counts;
        let timestamps2 = Object.keys(data).map(x => new Date(parseInt(x) * 1000));

        let resourceData = Object.values(data);
        let chartData = {};
        chartData['seriesData'] = [];
        for (let datapoint of resourceData) {
            for (let key of Object.keys(datapoint)) {
                if (!chartData[key]) chartData[key] = [];
                chartData[key].push(datapoint[key])
            }
        }


        for (let key of Object.keys(chartData)) {
            if (key == 'seriesData') continue;
            const series = {
                name: key,
                type: 'line',
                stack: key,
                data: chartData[key],
            };
            chartData['seriesData'].push(series);
        }

        let options = {
            chart: {
                renderTo: 'container',
                type: 'line'
            },
            title: {
                text: 'Resources Utilization Over Time',
            },

            subtitle: {
                text: 'Source: thesolarfoundation.com'
            },

            yAxis: {
                title: {
                    text: 'Amount of locks'
                }
            },
            xAxis: {
                type: 'datetime',
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            plotOptions: {
                series: {
                    // label: {
                    //     connectorAllowed: false
                    // },
                    // pointStart: 2010
                }
            },

            series: chartData['seriesData'].map(elem => {
                let ret = [];
                for (let index in elem.data) {
                    ret.push([timestamps2[index], elem.data[index]]);
                }
                elem.data = ret;
                return elem;
            }),

            // responsive: {
            //     rules: [{
            //         condition: {
            //             maxWidth: 500
            //         },
            //         chartOptions: {
            //             legend: {
            //                 layout: 'horizontal',
            //                 align: 'center',
            //                 verticalAlign: 'bottom'
            //             }
            //         }
            //     }]
            // }
        };
        return options;
    }
    getParallelData(data) {
        data = data.counts;
        let timestamps = Object.keys(data).map(x => Utils.formatTimestep(x));
        let timestamps2 = Object.keys(data).map(x => new Date(parseInt(x) * 1000));

        let options = {
            chart: {
                type: 'spline',
                parallelCoordinates: true,
                parallelAxes: {
                    lineWidth: 2
                }
            },
            title: {
                text: 'Marathon set'
            },
            plotOptions: {
                series: {
                    animation: false,
                    marker: {
                        enabled: false,
                        states: {
                            hover: {
                                enabled: false
                            }
                        }
                    },
                    states: {
                        hover: {
                            halo: {
                                size: 0
                            }
                        }
                    },
                    events: {
                        mouseOver: function () {
                            this.group.toFront();
                        }
                    }
                }
            },
            tooltip: {
                pointFormat: '<span style="color:{point.color}">\u25CF</span>' +
                '{series.name}: <b>{point.formattedValue}</b><br/>'
            },
            xAxis: {
                categories: [
                    'Training date',
                    'Miles for training run',
                    'Training time',
                    'Shoe brand',
                    'Running pace per mile',
                    'Short or long',
                    'After 2004'
                ],
                offset: 10
            },
            yAxis: [
                {
                    type: 'datetime',
                    tooltipValueFormat: '{value:%Y-%m-%d}'
                }, {
                    min: 0,
                    tooltipValueFormat: '{value} mile(s)'
                }, {
                    type: 'datetime',
                    min: 0,
                    labels: {
                        format: '{value:%H:%M}'
                    }
                }, {
                    categories: [
                        'Other',
                        'Adidas',
                        'Mizuno',
                        'Asics',
                        'Brooks',
                        'New Balance',
                        'Izumi'
                    ]
                },
                {
                    type: 'datetime'
                }, {
                    categories: ['&gt; 5miles', '&lt; 5miles']
                }, {
                    categories: ['Before', 'After']
                }
            ],
            colors: ['rgba(11, 200, 200, 0.1)'],
            series: data.map(function (set, i) {
                return {
                    name: 'Runner ' + i,
                    data: set,
                    shadow: false
                };
            })
        };
        return options;
    }

    getTreeMap(data) {
        console.log(data);
        let options = {
            series: [{
                type: "treemap",
                layoutAlgorithm: 'stripes',
                alternateStartingDirection: true,
                levels: [{
                    level: 1,
                    layoutAlgorithm: 'sliceAndDice',
                    dataLabels: {
                        enabled: true,
                        align: 'left',
                        verticalAlign: 'top',
                        style: {
                            fontSize: '15px',
                            fontWeight: 'bold'
                        }
                    }
                }],
                data: data
            }],
            title: {
                text: 'Locked Resources'
            }
        };
        return options;
    }

    private getSanky(data:any) {

        let sankyData = [];
        function follow(node) {
            for(let child of node.children) {
                let val = 1;
                if(child.value != undefined) val = (child.value) + 1;
                if(val < 1000) continue;
                // val = Math.log(val);
                if(val < 4) continue;
                sankyData.push([node.name, child.name, val]);
                follow(child);
            }
        }
        follow(data);
        console.log(sankyData);
        return {

            title: {
                text: 'Highcharts Sankey Diagram'
            },
            legend: {
                enabled: true
            },

            series: [{
                keys: ['from', 'to', 'weight'],
                data: sankyData,
                type: 'sankey',
                name: 'Sankey demo series'
            }]

        }
    }
    private getStreamGraph(data: any) {
        var colors = this.streamgraph.options.colors;
        let graphColor = [];
        for(let i  =0; i < data.series.length; i++) {
            graphColor.push(colors[i % 10])
        }
        let options = {

            chart: {
                type: 'area',
                // marginBottom: 30,
                zoomType: 'x'
            },

            // Make sure connected countries have similar colors
            colors: graphColor,

            title: {
                floating: true,
                align: 'left',
                text: 'Winter Olympic Medal Wins'
            },
            subtitle: {
                floating: true,
                align: 'left',
                y: 30,
                text: 'Source: <a href="https://www.sports-reference.com/olympics/winter/1924/">sports-reference.com</a>'
            },

            xAxis: {
                type: 'datetime',
                crosshair: true,
                categories: [''].concat(data.timesteps.map(x => new Date(parseInt(x) * 1000).getMinutes())),
                labels: {
                    align: 'left',
                    reserveSpace: false,
                    rotation: 270
                },
                dateformatter: {
                    hours: "%p %I",
                    minutes: "%p %I",
                },
                lineWidth: 1,
                tickWidth: 1
            },
            tooltip: {
              shared : true,
            },
            yAxis: {
                visible: true,
                startOnTick: true,
                endOnTick: true,
            },

            legend: {
                enabled: true
            },


            plotOptions: {
                area: {
                    stacking: 'normal',
                    lineColor: '#666666',
                    lineWidth: 1,
                    marker: {
                        enabled: false,
                        lineWidth: 1,
                        lineColor: '#666666'
                    }
                },
                // series: {
                //     label: {
                //         minFontSize: 5,
                //         maxFontSize: 15,
                //         style: {
                //             color: 'rgba(255,255,255,0.75)'
                //         }
                //     }
                // }
            },

            // Data parsed with olympic-medals.node.js
            series: data.series,

            exporting: {
                sourceWidth: 800,
                sourceHeight: 600
            },
        };
        return options;
    }

    private getTree(data: any) {
        return {
            tooltip: {
                trigger: 'item',
                triggerOn: 'mousemove'
            },
            series: [
                {
                    type: 'tree',

                    data: [data],

                    top: '1%',
                    left: '7%',
                    bottom: '1%',
                    right: '20%',

                    symbolSize: 7,

                    label: {
                        normal: {
                            position: 'left',
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 9
                        }
                    },

                    leaves: {
                        label: {
                            normal: {
                                position: 'right',
                                verticalAlign: 'middle',
                                align: 'left'
                            }
                        }
                    },

                    expandAndCollapse: true,
                    animationDuration: 550,
                    animationDurationUpdate: 750
                }
            ]
        }
    }
}
