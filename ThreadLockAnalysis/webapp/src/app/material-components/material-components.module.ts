import {NgModule} from '@angular/core';

import {MatTabsModule} from '@angular/material/tabs';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatSidenavModule} from '@angular/material/sidenav';


@NgModule({
    imports: [
        MatTabsModule,
        MatExpansionModule,
        MatProgressBarModule,
        MatSidenavModule,
    ],
    exports: [
        MatTabsModule,
        MatExpansionModule,
        MatProgressBarModule,
        MatSidenavModule,
    ]
})
export class MaterialComponentsModule {}
