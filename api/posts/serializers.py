from django.contrib.auth import get_user_model
from rest_framework import serializers

from .models import Post, PostImage
from categories.models import Category

class PostImageSerializer(serializers.ModelSerializer):
    image = serializers.ImageField()

    class Meta:
        model = PostImage
        fields = ['id', 'image']


class PostSerializer(serializers.ModelSerializer):
    title = serializers.CharField()
    content = serializers.CharField()
    category = serializers.PrimaryKeyRelatedField(queryset=Category.objects.all())
    author = serializers.HiddenField(default=serializers.CurrentUserDefault())
    price = serializers.IntegerField()
    location = serializers.CharField()
    phone_number = serializers.IntegerField()
    images = PostImageSerializer(source='post_set', many=True)

    class Meta:
        model = Post
        fields = ['id','title', 'content','category','author','created_on','price','location','phone_number', 'images']

    def to_representation(self, instance):
        rep = super().to_representation(instance)
        rep['category'] = instance.category.name
        rep['author'] = instance.author.email
        return rep
