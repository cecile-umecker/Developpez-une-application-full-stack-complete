import { Component } from '@angular/core';

@Component({
  selector: 'app-go-back',
  imports: [],
  templateUrl: './go-back.component.html',
  styleUrl: './go-back.component.scss'
})
export class GoBackComponent {

  goBack() {
    window.history.back();
  }
}
