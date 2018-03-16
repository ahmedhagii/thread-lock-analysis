import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceUtilizationComponent } from './resource-utilization.component';

describe('ResourceUtilizationComponent', () => {
  let component: ResourceUtilizationComponent;
  let fixture: ComponentFixture<ResourceUtilizationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResourceUtilizationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceUtilizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
