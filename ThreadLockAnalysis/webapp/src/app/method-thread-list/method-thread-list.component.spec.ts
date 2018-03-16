import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MethodThreadListComponent } from './method-thread-list.component';

describe('MethodThreadListComponent', () => {
  let component: MethodThreadListComponent;
  let fixture: ComponentFixture<MethodThreadListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MethodThreadListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MethodThreadListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
