from django.db import models
from django.contrib.auth import get_user_model

from categories.models import Category

User = get_user_model()

class Post(models.Model):
    title = models.CharField(max_length=60)
    content = models.TextField(max_length=1000)
    category = models.ForeignKey(Category, on_delete=models.DO_NOTHING)
    author = models.ForeignKey(User, on_delete=models.DO_NOTHING)
    created_on = models.DateTimeField(auto_now_add=True)
    price = models.IntegerField()
    location = models.CharField(max_length=40)
    phone_number = models.IntegerField()

    def __str__(self) -> str:
        return self.title

    class Meta:
        ordering = ['-created_on']

class PostImage(models.Model):
    post = models.ForeignKey(Post, default=None, on_delete=models.CASCADE, related_name='post_set')
    image = models.FileField(upload_to = 'images/')
 
    def __str__(self):
        return self.post.title