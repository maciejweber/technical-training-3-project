from django.contrib.auth import get_user_model
from rest_framework import serializers

from .models import Post
from categories.models import Category
from categories.serializers import CategorySerializer

User = get_user_model()


class PostSerializer(serializers.ModelSerializer):
    title = serializers.CharField()
    content = serializers.CharField()
    company = serializers.CharField()
    category = serializers.PrimaryKeyRelatedField(
        queryset=Category.objects.all(), many=True)
    author = serializers.ReadOnlyField(source='author.email')
    salary_from = serializers.IntegerField()
    salary_to = serializers.IntegerField()
    location = serializers.CharField()
    contact_email = serializers.CharField()

    class Meta:
        model = Post
        fields = ['id', 'title', 'company', 'content', 'category', 'author', 'created_on',
                  'salary_from', 'salary_to', 'location', 'contact_email']

    def to_representation(self, instance):
        rep = super().to_representation(instance)
        rep['category'] = CategorySerializer(instance.category, many=True).data
        return rep
