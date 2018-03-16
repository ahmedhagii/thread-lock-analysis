import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {ChartComponent} from "./chart/chart.component";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {ChartDialogComponent} from "./chart-dialog/chart-dialog.component";
import {ActivatedRoute, ParamMap, Route, Router, UrlSegment} from "@angular/router";
import {UploaderService} from "./uploader/uploader.service";
import {Utils} from "./utils/utils";
import {BigService} from "./big.service";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'My First Angular';
    selected : any;
    // dialog : MatDialog
    dialogRef: MatDialogRef<ChartDialogComponent>;

    constructor(public dialog: MatDialog,
                private router: Router,
                private route : ActivatedRoute,
                private location : Location,
                private bigService: BigService,
                private uploadService : UploaderService) {
        // this.router.navigate(['/']);

        this.uploadService.getNavChangeEmitter().subscribe(data => {
            // this.bigService.setParsedData(Utils.parseData(data));
        });
        // this.bigService.loadData().subscribe(data => {
        //     this.bigService.setParsedData(Utils.parseData(data));
        // });
    }

    ngOnInit() {
        // this.route.url.subscribe(url => {
        //     this.selected = location.path().split("/")[0];
        // })
        console.log(window.location.pathname.split("/")[1]);
        this.selected = window.location.pathname.split("/")[1];
    }

    onLinkClick(name) {
        this.router.navigate(['/', name]);
    }

    isSelected(link) {
        // console.log("hi ", this.selected, link);
        if(this.selected == link) return 'side-nav-item selected';
        return 'side-nav-item';
    }
}
