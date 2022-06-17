from django.db import models
from django.contrib.auth import get_user_model

from categories.models import Category

User = get_user_model()


class Post(models.Model):
    title = models.CharField(max_length=60)
    company = models.CharField(max_length=100)
    content = models.TextField(max_length=1000)
    category = models.ManyToManyField(Category)
    author = models.ForeignKey(User, on_delete=models.DO_NOTHING)
    created_on = models.DateTimeField(auto_now_add=True)
    salary_from = models.IntegerField()
    salary_to = models.IntegerField()
    location = models.CharField(max_length=40)
    contact_email = models.CharField(max_length=100)

    def __str__(self) -> str:
        return self.title

    class Meta:
        ordering = ['-created_on']
