from django.contrib.auth import get_user_model
from rest_framework import serializers
from datetime import timedelta, datetime

from .models import Post
from cities.models import City
from categories.models import Category
from categories.serializers import CategorySerializer
from cities.serializers import CitySerializer

User = get_user_model()


class PostSerializer(serializers.ModelSerializer):
    title = serializers.CharField()
    content = serializers.CharField(write_only=True)
    company = serializers.CharField()
    category = serializers.PrimaryKeyRelatedField(
        queryset=Category.objects.all(), many=True)
    salary_from = serializers.IntegerField()
    salary_to = serializers.IntegerField()
    location = serializers.PrimaryKeyRelatedField(
        queryset=City.objects.all())
    contact_email = serializers.CharField(write_only=True)
    formatted_date = serializers.SerializerMethodField(
        'get_formatted_date', read_only=True)

    class Meta:
        model = Post
        fields = ['id', 'title', 'company', 'content', 'category',
                  'salary_from', 'salary_to', 'location', 'contact_email', 'formatted_date']

    def to_representation(self, instance):
        rep = super().to_representation(instance)
        rep['category'] = CategorySerializer(instance.category, many=True).data
        rep['location'] = instance.location.name
        return rep

    def get_formatted_date(self, obj):
        time = datetime.now()
        if obj.created_on.minute == time.minute:
            return "Less than minute"
        if obj.created_on.hour == time.hour:
            return str(time.minute - obj.created_on.minute) + " minutes ago"
        elif obj.created_on.day == time.day:
            return str(time.hour - obj.created_on.hour) + " hours ago"
        elif obj.created_on.month == time.month:
            return str(time.day - obj.created_on.day) + " days ago"
        elif obj.created_on.year == time.year:
            return str(time.month - obj.created_on.month) + " months ago"
        return obj.created_on


class PostDetailSerializer(serializers.ModelSerializer):
    title = serializers.CharField()
    content = serializers.CharField()
    company = serializers.CharField()
    category = serializers.PrimaryKeyRelatedField(
        queryset=Category.objects.all(), many=True)
    author = serializers.ReadOnlyField(source='author.email')
    salary_from = serializers.IntegerField()
    salary_to = serializers.IntegerField()
    location = serializers.PrimaryKeyRelatedField(
        queryset=City.objects.all())
    contact_email = serializers.CharField()

    class Meta:
        model = Post
        fields = ['id', 'title', 'company', 'content', 'category', 'author', 'created_on',
                  'salary_from', 'salary_to', 'location', 'contact_email']

    def to_representation(self, instance):
        rep = super().to_representation(instance)
        rep['category'] = CategorySerializer(instance.category, many=True).data
        rep['location'] = CitySerializer(instance.location).data
        return rep
