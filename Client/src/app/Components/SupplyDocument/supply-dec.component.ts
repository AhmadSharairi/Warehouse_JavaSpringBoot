import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SupplyDocumentInfo } from '../../shared/models/SupplyDocumentInfo';
import { SupplyDocumentService } from './supply-document.service';

@Component({
  selector: 'supply',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supply-dec.component.html',
  styleUrls: ['./supply-dec.component.css'],
})
export class SupplyDecComponent implements OnInit {
  supplys: SupplyDocumentInfo[] = [];
  userName: string = '';
  curruntRoleName: string = '';

  constructor(
    private supplyService: SupplyDocumentService,
    private toastr: ToastrService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeUser();
    this.loadSupplys();
  }

  private initializeUser(): void {
    this.userName = this.authService.getUserNameFromToken();
    this.curruntRoleName = this.authService.getRoleFromToken();
  }

  private loadSupplys(): void {
    const fetchSupplyDocuments = this.curruntRoleName === 'Manager' 
      ? this.supplyService.getAllSupplyDocuments() 
      : this.supplyService.getSupplyDocumentsByCreated(this.userName);

    fetchSupplyDocuments.subscribe({
      next: (data: SupplyDocumentInfo[]) => {
        this.supplys = data;
        console.log(this.supplys);
      },
      error: this.handleError('fetching supply documents'),
    });
  }

  changeSupplyStatus(supplyId: number, status: string): void {
    this.supplyService.changeSupplyDocumentStatus(supplyId, status).subscribe({
      next: () => {
        const supplyToUpdate = this.supplys.find(supply => supply.id === supplyId);
        if (supplyToUpdate) {
          supplyToUpdate.status = status;
        }
        this.loadSupplys();
      },
      error: this.handleError('changing supply status'),
    });
  }

  deleteSupply(id: number): void {
    if (confirm('Are you sure you want to delete this document?')) {
      this.supplyService.deleteSupplyDocument(id).subscribe({
        next: () => {
          this.toastr.success('Document deleted successfully.');
          this.loadSupplys();
        },
        error: this.handleError('deleting document'),
      });
    }
  }

  private handleError(action: string) {
    return (error: any) => {
      console.error(`Error ${action}:`, error);
      this.toastr.error(`Error ${action}. Please try again.`);
    };
  }

  addDocument(){
    this.router.navigate(['/add-documnents']);
  }
}
