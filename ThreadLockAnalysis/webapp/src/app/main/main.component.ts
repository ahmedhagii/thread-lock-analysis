import {AfterViewInit, Component, OnInit} from '@angular/core';
import {UploaderService} from "../uploader/uploader.service";
import {Utils} from "../utils/utils";
import {BigService} from "../big.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(private _fileUploadedService: UploaderService,
              private bigService : BigService, private router : Router) {
      this.bigService.loadData().subscribe(data => {
          // this.bigService.setParsedData(Utils.parseData(data));
      });
  }

  ngOnInit() {

  }

  onLinkClick(name) {
      this.router.navigate(['/deadlocks']);
  }
}
