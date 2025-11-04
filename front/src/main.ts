import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { BrowserModule } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';

import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(BrowserModule),
    provideRouter(routes)
  ]
})
.catch(err => console.error(err));
