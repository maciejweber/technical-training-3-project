from rest_framework import serializers
from .models import Category

class CategorySerializer(serializers.Serializer):
    name = serializers.CharField(max_length=30)

    class Meta:
        model = Category
        fields = '__all__'