import { TestBed, inject } from '@angular/core/testing';

import { BigService } from './big.service';

describe('BigService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BigService]
    });
  });

  it('should be created', inject([BigService], (service: BigService) => {
    expect(service).toBeTruthy();
  }));
});
