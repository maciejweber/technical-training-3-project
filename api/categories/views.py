from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response

from .serializers import CategorySerializer
from .models import Category

class CategoryView(APIView):
    serializer_class = CategorySerializer

    def get(self, request, format=None):
        estates = Category.objects.all()
        serializer = CategorySerializer(estates, many=True)
        return Response(serializer.data)