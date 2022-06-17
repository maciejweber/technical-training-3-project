from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response

from rest_framework.generics import ListCreateAPIView
from rest_framework.views import APIView

from rest_framework import status

from .serializers import PostSerializer
from .models import Post

# class PostListView(ListCreateAPIView):
#     permission_classes = [IsAuthenticated]
#     serializer_class = PostSerializer

#     def get_queryset(self):
#         return Post.objects.all()

#     def perform_create(self, serializer):
#         data = serializer.data
#         print(data.images)
#         serializer.save(author=self.request.user)


class PostListView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = PostSerializer

    def get(self, request, format=None):
        posts = Post.objects.all()
        serializer = PostSerializer(posts, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = PostSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save(author=self.request.user)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
