from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response

from rest_framework.generics import ListCreateAPIView

from .serializers import PostSerializer
from .models import Post

class PostListView(ListCreateAPIView):
    # permission_classes = [IsAuthenticated]
    serializer_class = PostSerializer

    def get_queryset(self):
        return Post.objects.all()

    def perform_create(self, serializer):
        serializer.save()
        # serializer.save(author=self.request.user)