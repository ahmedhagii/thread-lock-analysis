import {Component, OnInit, ViewChild} from '@angular/core';
import {ArrayType} from "@angular/compiler/src/output/output_ast";
import {UploaderService} from "./uploader.service";

@Component({
    selector: 'uploader',
    templateUrl: './uploader.component.html',
    styleUrls: ['./uploader.component.css']
})
export class UploaderComponent implements OnInit {

    @ViewChild('fileImportInput')
    fileImportInput: any;
    public data : any;


    constructor(private _fileUploadedService : UploaderService) {}

    ngOnInit() {
    }

    getData() {
        return this.data;
    }

    fileChangeListener($event): void {
        var target = $event.target || $event.srcElement;
        var csvFile = target.files[0];

        var reader = new FileReader();
        reader.readAsText(csvFile);
        reader.onload = () => {
            let csvData = reader.result
            let records = csvData.split(/\n/);
            let columnNames = ["id", "name", "priority", "status", "timestamp"]



            let getColumn = (data, col) => {
                let index = columnNames.indexOf(col);
                let ret = []
                for(let record of data) {
                    ret.push(record.split(",")[index])
                }
                return ret
            }


            this.data = new Object();
            this.data.all = records;
            this.data.priority = getColumn(records, "priority").map(entry => parseInt(entry));
            this.data.timestamp = getColumn(records, "timestamp").map(entry => new Date(entry*1000));

            console.log(this.data.timestamp)

            this._fileUploadedService.emitDataChange(this.data);
        }
    }
}
