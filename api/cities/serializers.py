from rest_framework import serializers
from .models import City


class CitySerializer(serializers.Serializer):
    id = serializers.ReadOnlyField()
    name = serializers.CharField(max_length=30)

    class Meta:
        model = City
        fields = '__all__'
