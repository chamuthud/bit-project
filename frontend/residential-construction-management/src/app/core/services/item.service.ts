import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Decimal } from 'decimal.js'; 

// Base URLs
const VENDOR_API_URL = 'http://localhost:8080/api/vendor/items'; 
const PUBLIC_API_URL = 'http://localhost:8080/api/items'; 


// Interface matches ItemResponse DTO
export interface Item {
    itemId: number;
    itemName: string;
    description?: string;
    unitPrice: number | string | Decimal; 
    qtyOnHand: number;
    unitOfMeasure?: string;
    vendorId: number;
    vendorUsername?: string;
    createdAt?: string;
    updatedAt?: string;
}

// Interface matches ItemRequest DTO
export interface ItemRequestPayload {
    itemName: string;
    description?: string;
    unitPrice: number | string; 
    qtyOnHand: number;
    unitOfMeasure?: string;
}


@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor(private http: HttpClient) { }

  // --- Vendor-Specific Methods ---

  // Get items listed by the currently authenticated vendor
  getMyItems(): Observable<Item[]> {
    return this.http.get<Item[]>(VENDOR_API_URL + 'my');
  }

  // Get a specific item owned by the current vendor
  getMyItemById(itemId: number): Observable<Item> {
     return this.http.get<Item>(VENDOR_API_URL + 'my/' + itemId);
  }

  // Create a new item for the current vendor
  createItem(itemData: ItemRequestPayload): Observable<Item> {
    return this.http.post<Item>(VENDOR_API_URL, itemData);
  }

  // Update an item owned by the current vendor
  updateItem(itemId: number, itemData: ItemRequestPayload): Observable<Item> {
    return this.http.put<Item>(VENDOR_API_URL + itemId, itemData);
  }

  // Delete an item owned by the current vendor
  deleteItem(itemId: number): Observable<any> {
    return this.http.delete(VENDOR_API_URL + itemId);
  }


}