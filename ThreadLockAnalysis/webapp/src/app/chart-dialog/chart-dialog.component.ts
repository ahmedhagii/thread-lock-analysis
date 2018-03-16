import {AfterViewInit, Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
    selector: 'app-chart-dialog',
    templateUrl: './chart-dialog.component.html',
    styleUrls: ['./chart-dialog.component.css']
})
export class ChartDialogComponent implements AfterViewInit {

    @ViewChild('plot') plotHolder: ElementRef;

    constructor(public dialogRef: MatDialogRef<ChartDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any) {}

    ngAfterViewInit(): void {
        this.plot(this.data)
    }

    plot(data) {
        console.log(this.plotHolder)
        let element = this.plotHolder.nativeElement;
        // var layout = {
        //     title: 'Frequency of Diff Thread Priorities Over Time',
        //     width: 900,
        //     xaxis: {
        //         title: 'Time',
        //         titlefont: {
        //             family: 'Courier New, monospace',
        //             size: 18,
        //             color: '#7f7f7f'
        //         }
        //     },
        //     yaxis: {
        //         title: 'Count',
        //         titlefont: {
        //             family: 'Courier New, monospace',
        //             size: 18,
        //             color: '#7f7f7f'
        //         }
        //     }
        // };
        data.layout.width = 900
        data.layout.height = 600
        Plotly.plot(element, data.data, data.layout)
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}

