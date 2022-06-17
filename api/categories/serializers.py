from rest_framework import serializers
from .models import Category


class CategorySerializer(serializers.Serializer):
    id = serializers.ReadOnlyField()
    name = serializers.CharField(max_length=30)

    class Meta:
        model = Category
        fields = '__all__'
