import { Component, OnInit } from '@angular/core';
import {Utils} from "../utils/utils";
import {UploaderService} from "../uploader/uploader.service";
import {BigService} from "../big.service";

@Component({
  selector: 'app-dummy',
  templateUrl: './dummy.component.html',
  styleUrls: ['./dummy.component.css']
})
export class DummyComponent implements OnInit {

  constructor(private _fileUploadedService : UploaderService,
              private _bigService : BigService) {
  }

  ngOnInit() {

  //     this._bigService.waitForData().subscribe(data => {
  //
  //     });
  //     let records = Utils.data.split(/\n/);
  //     let columnNames = ["id", "name", "priority", "status", "timestamp"]
  //     let getColumn = (data, col) => {
  //         let index = columnNames.indexOf(col);
  //         let ret = []
  //         for(let record of data) {
  //             ret.push(record.split(",")[index])
  //         }
  //         return ret
  //     };
  //
  //
  //     let data = {};
  //     data['all'] = records;
  //     data['priority'] = getColumn(records, "priority").map(entry => parseInt(entry));
  //     data['timestamp'] = getColumn(records, "timestamp").map(entry => new Date(entry*1000));
  //
  //     console.log('hahaha');
  //     console.log(records.splice(1, 5));
  //     this._fileUploadedService.emitDataChange(data);
  }

}
