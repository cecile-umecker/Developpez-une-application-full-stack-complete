import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { localAuthGuardGuard } from './local-auth.guard';

describe('localAuthGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => localAuthGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
