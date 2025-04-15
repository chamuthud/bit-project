import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorItemFormComponent } from './vendor-item-form.component';

describe('VendorItemFormComponent', () => {
  let component: VendorItemFormComponent;
  let fixture: ComponentFixture<VendorItemFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VendorItemFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VendorItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
