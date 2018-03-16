import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Subject} from "rxjs/Subject";

@Injectable()
export class BigService {

    httpOptions: any;
    host : string;
    parsedData : any = new Subject();
    parsedDataValue : any;
    loaded : boolean = false;

    constructor(private http: HttpClient) {
        this.httpOptions = {
            headers: new HttpHeaders({
                'Access-Control-Allow-Origin': '*',
            })
        };
        this.host = environment.host;
    }

    loadData() {
        let url = '/get-data';
        console.log("here");
        return this.http.get(this.host +  url, this.httpOptions);
    }

    setParsedData(parsedData) {
        this.loaded = true;
        this.parsedDataValue = parsedData;
        this.parsedData.next(parsedData);
    }

    waitForData() {
        return this.parsedData.asObservable()
    }

    isReady() {
        return this.loaded;
    }

    getMethodData(methodName: string) {
        let url = '/threads/method/' + methodName.replace("(Native Method)", "");
        console.log(url);
        return this.http.get(this.host +  url, this.httpOptions);
    }

    getThreadListTimestamp(timestamp: any) {
        timestamp = timestamp.replace("\n", " ");
        console.log(timestamp);
        timestamp = new Date(timestamp);
        console.log(timestamp);
        timestamp = (timestamp.getTime() / 1000).toFixed(0)
        let url = '/threads/time/' + timestamp;
        console.log(url);
        return this.http.get(this.host +  url, this.httpOptions);
    }

    getThreadDataByName(name: string) {
        let url = '/thread/' + name;
        console.log(url);
        return this.http.get(this.host +  url, this.httpOptions);
    }

    getResourcesUtil(dumpName : string) {
        let url = '/resources/' + dumpName;
        return this.http.get(this.host + url, this.httpOptions);
    }

    getTreeMapData(dumpName: string) {
        let url = '/resources/treemap/' + dumpName;
        return this.http.get(this.host + url, this.httpOptions);
    }

    getStreamGraph(dumpName: string | null) {
        let url = '/resources/streamgraph/' + dumpName;
        return this.http.get(this.host + url, this.httpOptions)
    }

    getTreeData(dumpName: string | null) {
        let url = '/resources/tree/' + dumpName;
        return this.http.get(this.host + url, this.httpOptions)
    }
}
