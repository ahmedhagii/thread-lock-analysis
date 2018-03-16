import { Component, OnInit } from '@angular/core';
import {Utils} from "../utils/utils";
import {BigService} from "../big.service";
import {UploaderService} from "../uploader/uploader.service";

@Component({
  selector: 'app-thread-summary',
  templateUrl: './thread-summary.component.html',
  styleUrls: ['./thread-summary.component.css']
})
export class ThreadSummaryComponent implements OnInit {

  constructor(private bigService : BigService, private uploadService : UploaderService) {
      this.uploadService.getNavChangeEmitter().subscribe(data => {
          this.bigService.setParsedData(Utils.parseData(data));
      });
      this.bigService.loadData().subscribe(data => {
          // this.bigService.setParsedData(Utils.parseData(data));
      });
      // this.bigService.loadData().subscribe(data => {
      //     this.bigService.setParsedData(Utils.parseData(data));
      // });
  }

  ngOnInit() {
  }

}
