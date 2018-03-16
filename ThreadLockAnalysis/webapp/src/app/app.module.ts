import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import {AppComponent} from './app.component';
import {ChartComponent} from './chart/chart.component';
import {UploaderComponent} from './uploader/uploader.component';
import {UploaderService} from "./uploader/uploader.service";
import {ChartDialogComponent} from './chart-dialog/chart-dialog.component';
import {MatDialogModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NgxEchartsModule } from 'ngx-echarts';
import { StatusListComponent } from './status-list/status-list.component';
import { MainComponent } from './main/main.component';
import {MaterialComponentsModule} from "./material-components/material-components.module";
import { DummyComponent } from './dummy/dummy.component';
import { MethodThreadListComponent } from './method-thread-list/method-thread-list.component';
import { HttpClientModule } from '@angular/common/http';
import {BigService} from "./big.service";
import { DeadlocksComponent } from './deadlocks/deadlocks.component';
import { ThreadSummaryComponent } from './thread-summary/thread-summary.component';
import { MethoddUsedComponent } from './methodd-used/methodd-used.component';
import { ResourceUtilizationComponent } from './resource-utilization/resource-utilization.component';


import { ChartModule , HIGHCHARTS_MODULES } from 'angular-highcharts';
import * as more from 'highcharts/highcharts-more.src';
import * as treemap from 'highcharts/modules/treemap.src';
import * as streamgraph from 'highcharts/modules/streamgraph.src';
import * as sankey from 'highcharts/modules/sankey.src';

export function highchartsModules() {
    // apply Highcharts Modules to this array
    return [ more, treemap, streamgraph, sankey ];
}

const appRoutes: Routes = [
    { path: 'resources/:dumpName', component: ResourceUtilizationComponent },
    { path: 'thread-summary/status/:time', component: StatusListComponent },
    { path: 'thread-summary/status/:status', component: StatusListComponent },
    { path: 'threads/method/:methodName', component: MethodThreadListComponent },
    { path: 'deadlocks', component: DeadlocksComponent },
    { path: 'thread-summary', component: ThreadSummaryComponent },
    { path: '', redirectTo: '/thread-summary', pathMatch: 'full' },
    // { path: 'heroes', component: HeroListComponent },
];


@NgModule({
    exports: [
        MatDialogModule,
    ],
    declarations: [
        AppComponent,
        ChartComponent,
        UploaderComponent,
        ChartDialogComponent,
        StatusListComponent,
        DummyComponent,
        MethodThreadListComponent,
        DeadlocksComponent,
        ThreadSummaryComponent,
        MethoddUsedComponent,
        ResourceUtilizationComponent,
    ],
    imports: [
        ChartModule,
        RouterModule.forRoot(
            appRoutes,
            // { enableTracing: true } // <-- debugging purposes only
        ),
        BrowserModule,
        BrowserAnimationsModule,
        NgxEchartsModule,
        MaterialComponentsModule,
        HttpClientModule,
    ],
    providers: [UploaderService, MatDialogModule, BigService, { provide: HIGHCHARTS_MODULES, useFactory: highchartsModules }],
    entryComponents: [ChartDialogComponent],
    bootstrap: [AppComponent]
})
export class AppModule {
}
