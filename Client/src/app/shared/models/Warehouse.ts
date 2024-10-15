import { Item } from "./Item";

export interface Warehouse {
  id?: number;
  warehouseName: string;
  warehouseDescription: string;
  createdBy: string;
  items: Item[];

}
