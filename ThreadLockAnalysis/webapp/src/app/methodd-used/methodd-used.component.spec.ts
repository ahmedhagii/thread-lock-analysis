import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MethoddUsedComponent } from './methodd-used.component';

describe('MethoddUsedComponent', () => {
  let component: MethoddUsedComponent;
  let fixture: ComponentFixture<MethoddUsedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MethoddUsedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MethoddUsedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
