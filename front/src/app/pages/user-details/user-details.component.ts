import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { User, UserUpdate } from 'src/app/models/user.model';
import { Topic } from 'src/app/models/topic.model';
import { UserService } from 'src/app/core/services/user.service';
import { TopicService } from 'src/app/core/services/topic.service';

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit, OnDestroy {

  user: User = { username: '', email: '' };
  editData: UserUpdate = {};
  subscriptions: Topic[] = [];

  private userSub?: Subscription;
  private topicsSub?: Subscription;

  constructor(private userService: UserService, private topicService: TopicService) {}

  ngOnInit(): void {
    // récupère les infos du user
    this.userSub = this.userService.getUser().subscribe({
      next: (user) => {
        this.user = user;
        this.editData.username = user.username;
        this.editData.email = user.email;
      },
      error: (err) => console.error('Erreur récupération user', err)
    });

    // récupère les topics abonnés de l'utilisateur
    this.topicsSub = this.topicService.getUserTopics().subscribe({
      next: (topics) => this.subscriptions = topics,
      error: (err) => console.error('Erreur récupération topics', err)
    });
  }

  onSubmit(): void {
    this.userService.updateUser(this.editData).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        console.log('Profil mis à jour', updatedUser);
      },
      error: (err) => console.error('Erreur mise à jour profil', err)
    });
  }

  onUnsubscribe(topicId: number): void {
    this.topicService.unsubscribeFromTopic(topicId).subscribe({
      next: (updatedTopic) => {
        this.subscriptions = this.subscriptions.filter(t => t.id !== updatedTopic.id);
        console.log('Désabonné de', updatedTopic.title);
      },
      error: (err) => console.error('Erreur désabonnement', err)
    });
  }

  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
    this.topicsSub?.unsubscribe();
  }
}
